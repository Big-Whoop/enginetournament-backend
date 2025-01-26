package de.fahsel.chess.enginetournament.service.system;

import de.fahsel.chess.enginetournament.model.system.ReactiveProcess;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class ReactiveProcessRunner {
    private final Map<Long, Process> processMap = new ConcurrentHashMap<>();

    public ReactiveProcess run(String path, String initialCommand) {
        DirectProcessor<String> inProcessor = DirectProcessor.create();
        FluxSink<String> userInput = inProcessor.sink();
        Flux<String> flux = Flux.create(stringFluxSink -> createProcessFlux(path, initialCommand, inProcessor, stringFluxSink));

        return ReactiveProcess.builder()
                .input(userInput)
                .output(flux)
                .build();
    }

    public void killAll() {
        processMap.values().forEach(Process::destroyForcibly);
    }

    private void createProcessFlux(String path, String initialCommand, DirectProcessor<String> inProcessor, FluxSink<String> stringFluxSink) {
        ProcessBuilder processBuilder = new ProcessBuilder(path);

        try {
            Process process = processBuilder.start();

            processMap.put(process.pid(), process);

            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                sendCommand(initialCommand, writer);

                inProcessor.subscribe(s -> handleUserInput(writer, s));

                handleProcessOutput(stringFluxSink, process);

                process.waitFor();
                stringFluxSink.complete();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleProcessOutput(FluxSink<String> stringFluxSink, Process process) {
        Scanner scanner = new Scanner(process.getInputStream());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            log.info("<- " + line);

            stringFluxSink.next(line);
        }
    }

    private void handleUserInput(OutputStreamWriter writer, String s) {
        try {
            sendCommand(s, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String command, OutputStreamWriter writer) throws IOException {
        log.info("-> " + command);

        writer.write(command + "\n");
        writer.flush();
    }
}
