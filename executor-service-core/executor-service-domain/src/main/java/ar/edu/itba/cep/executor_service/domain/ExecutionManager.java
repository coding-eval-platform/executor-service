package ar.edu.itba.cep.executor_service.domain;


import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor.models.ExecutionResponse;
import ar.edu.itba.cep.executor_service.runner.CodeRunner;
import ar.edu.itba.cep.executor_service.services.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A manager in charge of processing {@link ExecutionRequest}s.
 */
@Service
public class ExecutionManager implements ExecutorService {

    /**
     * The {@link CodeRunner} used to process an {@link ExecutionRequest}.
     */
    private final CodeRunner codeRunner;


    @Autowired
    public ExecutionManager(final CodeRunner codeRunner) {
        this.codeRunner = codeRunner;
    }


    @Override
    public ExecutionResponse processExecutionRequest(final ExecutionRequest executionRequest)
            throws IllegalArgumentException {
        return codeRunner.processExecutionRequest(executionRequest);
    }
}
