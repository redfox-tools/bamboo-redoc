package tools.redfox.bamboo.redoc.model;

import com.atlassian.bamboo.specs.api.codegen.annotations.Builder;
import com.atlassian.bamboo.specs.api.model.AtlassianModuleProperties;
import com.atlassian.bamboo.specs.api.model.plan.condition.ConditionProperties;
import com.atlassian.bamboo.specs.api.model.plan.requirement.RequirementProperties;
import com.atlassian.bamboo.specs.api.model.task.TaskProperties;
import org.jetbrains.annotations.NotNull;
import tools.redfox.bamboo.redoc.builders.ReDocTask;

import java.util.List;

@Builder(ReDocTask.class)
public class ReDocTaskProperties extends TaskProperties {
    private static final AtlassianModuleProperties ATLASSIAN_PLUGIN =
            new AtlassianModuleProperties("tools.redfox.bamboo.redoc:redoc");
    private String executable;
    private String environmentVariables;
    private String workingDirectory;
    private String options;

    public ReDocTaskProperties() {
    }

    public ReDocTaskProperties(String description, boolean taskEnabled, String executable, String options, String environmentVariables, String workingDirectory, List<RequirementProperties> requirements, List<ConditionProperties> conditions) {
        super(description, taskEnabled, requirements, conditions);
        this.options = options;
        this.executable = executable;
        this.environmentVariables = environmentVariables;
        this.workingDirectory = workingDirectory;
    }

    public String getOptions() {
        return options;
    }

    public String getExecutable() {
        return executable;
    }

    public String getEnvironmentVariables() {
        return environmentVariables;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @NotNull
    @Override
    public AtlassianModuleProperties getAtlassianPlugin() {
        return ATLASSIAN_PLUGIN;
    }
}
