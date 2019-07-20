package tools.redfox.bamboo.redoc.builders;

import com.atlassian.bamboo.specs.api.builders.task.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tools.redfox.bamboo.redoc.model.ReDocTaskProperties;

public class ReDocTask extends Task<ReDocTask, ReDocTaskProperties> {
    @Nullable
    protected String options;

    @Nullable
    protected String environmentVariables;

    @Nullable
    protected String workingDirectory;

    @NotNull
    protected String executable;

    public ReDocTask options(@Nullable String options) {
        this.options = options;
        return this;
    }

    public ReDocTask environmentVariables(@Nullable String environmentVariables) {
        this.environmentVariables = environmentVariables;
        return this;
    }

    public ReDocTask workingDirectory(@Nullable String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    public ReDocTask executable(@NotNull String executable) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    @NotNull
    @Override
    protected ReDocTaskProperties build() {
        return new ReDocTaskProperties(
                description,
                taskEnabled,
                executable,
                options,
                environmentVariables,
                workingDirectory,
                requirements,
                conditions
        );
    }
}
