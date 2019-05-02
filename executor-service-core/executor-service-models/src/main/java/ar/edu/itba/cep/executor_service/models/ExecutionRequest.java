package ar.edu.itba.cep.executor_service.models;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Represents an execution request.
 */
public class ExecutionRequest {

    /**
     * The execution request's id.
     */
    private final long id;

    /**
     * The code to be run.
     */
    private final String code;

    /**
     * The input arguments to be passed to the execution.
     */
    private final List<String> inputs;

    /**
     * The time given to execute, in milliseconds..
     */
    private final Long timeout;

    /**
     * The programming language in which the {@link #code} is written.
     */
    private final Language language;


    /**
     * Constructor.
     *
     * @param code     The code to be run.
     * @param inputs   The input arguments to be passed to the execution.
     * @param timeout  The time given to execute, in milliseconds.
     * @param language The programming language in which the {@code code} is written.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    public ExecutionRequest(final String code, final List<String> inputs, final Long timeout, final Language language)
            throws IllegalArgumentException {
        assertCode(code);
        assertInputsList(inputs);
        assertLanguage(language);
        this.id = 0;
        this.code = code;
        this.inputs = inputs;
        this.timeout = timeout;
        this.language = language;
    }


    /**
     * @return The execution request's id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The code to be run.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The input arguments to be passed to the execution.
     */
    public List<String> getInputs() {
        return inputs;
    }

    /**
     * @return The time given to execute, in milliseconds.
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * @return The programming language in which the {@link #getCode()} is written.
     */
    public Language getLanguage() {
        return language;
    }

    // ================================
    // equals, hashcode and toString
    // ================================

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionRequest)) {
            return false;
        }
        final var that = (ExecutionRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", inputs=" + inputs +
                ", timeout=" + timeout +
                ", language=" + language +
                '}';
    }


    // ================================
    // Assertions
    // ================================

    /**
     * Asserts that the given {@code code} is valid.
     *
     * @param code The code to be checked.
     * @throws IllegalArgumentException If the {@code code} is not valid.
     */
    private static void assertCode(final String code) throws IllegalArgumentException {
        Assert.notNull(code, "The code must not be null.");
    }

    /**
     * Asserts that the given {@code inputs} {@link List} is valid.
     *
     * @param inputs The inputs {@link List} to be checked.
     * @throws IllegalArgumentException If the {@code inputs} {@link List} is not valid.
     */
    private static void assertInputsList(final List<String> inputs) throws IllegalArgumentException {
        Assert.notNull(inputs, "The inputs list must not be null");
        Assert.isTrue(inputs.stream().noneMatch(Objects::isNull), "The inputs list must not contain nulls.");
    }

    /**
     * Asserts that the given {@code language} is valid.
     *
     * @param language The {@link Language} to be checked.
     * @throws IllegalArgumentException If the {@code language} is not valid.
     */
    private static void assertLanguage(final Language language) throws IllegalArgumentException {
        Assert.notNull(language, "The language must not be null");
    }
}