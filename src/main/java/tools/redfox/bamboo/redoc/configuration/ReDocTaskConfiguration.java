package tools.redfox.bamboo.redoc.configuration;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskConfiguratorHelper;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskRequirementSupport;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.v2.build.agent.capability.RequirementImpl;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ReDocTaskConfiguration extends AbstractTaskConfigurator implements TaskRequirementSupport {
    private UIConfigSupport uiConfigSupport;

    List<String> fields = Arrays.asList("tools.redfox.redoc.spec", "tools.redfox.redoc.options",
            "tools.redfox.redoc.executable", "tools.redfox.redoc.environmentVariable", "tools.redfox.redoc.workingDirectory");

    public ReDocTaskConfiguration(@ComponentImport UIConfigSupport uiConfigSupport, @ComponentImport TaskConfiguratorHelper taskConfiguratorHelper) {
        this.uiConfigSupport = uiConfigSupport;
        setTaskConfiguratorHelper(taskConfiguratorHelper);
    }

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        Map<String, String> map = super.generateTaskConfigMap(params, previousTaskDefinition);
        this.taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(map, params, fields);
        return map;
    }

    @Override
    public Set<Requirement> calculateRequirements(TaskDefinition taskDefinition) {
        taskDefinition.getConfiguration();
        return new LinkedHashSet<Requirement>() {{
            add(new RequirementImpl(
                    "system.builder.redoc." + taskDefinition.getConfiguration().get("tools.redfox.redoc.executable"),
                    true,
                    ".*"
            ));
        }};
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put("uiConfig", uiConfigSupport);
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        context.put("uiConfig", uiConfigSupport);
        this.taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, fields);
    }
}
