package tools.redfox.bamboo.redoc.type;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinitionContextImpl;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinitionImpl;
import com.atlassian.bamboo.process.CommandlineStringUtils;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.security.SecureToken;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ReDocTaskType implements TaskType {
    private ProcessService processService;
    private EnvironmentVariableAccessor environmentVariableAccessor;
    private CapabilityContext capabilityContext;

    public ReDocTaskType(
            @ComponentImport final ProcessService processService,
            @ComponentImport EnvironmentVariableAccessor environmentVariableAccessor,
            @ComponentImport CapabilityContext capabilityContext
    ) {
        this.processService = processService;
        this.environmentVariableAccessor = environmentVariableAccessor;
        this.capabilityContext = capabilityContext;
    }

    @Override
    public TaskResult execute(TaskContext taskContext) throws TaskException {
        BuildLogger buildLogger = taskContext.getBuildLogger();
        ConfigurationMap configurationMap = taskContext.getConfigurationMap();
        String executable = configurationMap.get("tools.redfox.redoc.executable");
        String command = String.join(
                " ",
                this.capabilityContext.getCapabilityValue("system.builder.redoc." + executable),
                "bundle",
                configurationMap.getOrDefault("tools.redfox.redoc.options", ""),
                configurationMap.get("tools.redfox.redoc.spec")
        );

        Map<String, String> extraEnvironmentVariables = this.environmentVariableAccessor
                .splitEnvironmentAssignments(configurationMap.get("tools.redfox.redoc.environmentVariables"), false);

        List<String> arguments = CommandlineStringUtils.tokeniseCommandline(command);
        ImmutableList.Builder<String> commandListBuilder = ImmutableList.builder();
        commandListBuilder.addAll(arguments);

        buildLogger.addBuildLogEntry("Exec: " + String.join(" ", commandListBuilder.build()));

        TaskResultBuilder taskResultBuilder = TaskResultBuilder.newBuilder(taskContext);

        taskResultBuilder.checkReturnCode(
                this.processService.executeExternalProcess(
                        taskContext,
                        (new ExternalProcessBuilder())
                                .command(commandListBuilder.build())
                                .env(extraEnvironmentVariables)
                                .workingDirectory(
                                        new File(FilenameUtils.concat(
                                                taskContext.getWorkingDirectory().getAbsolutePath(),
                                                taskContext.getConfigurationMap().getOrDefault("tools.redfox.redoc.workingDirectory", "")
                                        ))
                                )
                )
        );

        taskContext.getBuildContext().getArtifactContext().getDefinitionContexts().add(
                new ArtifactDefinitionContextImpl(
                        new ArtifactDefinitionImpl(
                                "ReDoc Documentation",
                                taskContext.getWorkingDirectory().getAbsolutePath(),
                                "redoc-static.html"
                        ),
                        SecureToken.create()
                )
        );

        return taskResultBuilder.build();
    }
}
