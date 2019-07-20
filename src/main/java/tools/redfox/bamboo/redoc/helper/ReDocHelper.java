package tools.redfox.bamboo.redoc.helper;

import com.atlassian.bamboo.build.artifact.ArtifactLink;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.chains.ChainStageResult;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.resultsummary.BuildResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.task.TaskDefinition;
import org.springframework.util.StringUtils;
import tools.redfox.bamboo.redoc.type.ReDocTaskType;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tools.redfox.bamboo.redoc.type.ReDocTaskType.PLUGIN_KEY;

public class ReDocHelper {
    public static Set<String> getAvailableArtifactNames(ResultsSummary resultsSummary) {
        return getAvailableArtifactNames(resultsSummary.getImmutablePlan().getBuildDefinition().getTaskDefinitions());
    }

    public static Set<String> getAvailableArtifactNames(List<TaskDefinition> taskDefinitions) {
        Set<String> artifactNames = new LinkedHashSet<>();
        for (TaskDefinition task : taskDefinitions) {
            if (!task.getPluginKey().equals(PLUGIN_KEY)) {
                continue;
            }
            artifactNames.add(StringUtils.isEmpty(task.getUserDescription()) ? ReDocTaskType.DEFAULT_TITLE : task.getUserDescription());
        }
        return artifactNames;
    }

    public static Set<String> getAvailableArtifactNames(ChainResultsSummary resultsSummary) {
        Set<String> artifactNames = new LinkedHashSet<>();
        for (ImmutableJob job : resultsSummary.getImmutablePlan().getAllJobs()) {
            artifactNames.addAll(getAvailableArtifactNames(job.getTaskDefinitions()));
        }

        return artifactNames;
    }

    public static ArtifactLink findReDocArtifact(Collection<ArtifactLink> artifactLinkCollection, Set<String> artifactNames) {
        for (ArtifactLink link : artifactLinkCollection) {
            if (!artifactNames.contains(link.getArtifact().getLabel())) {
                continue;
            }
            return link;
        }
        return null;
    }

    public static Set<ArtifactLink> findAllArtifacts(ChainResultsSummary resultsSummary, Set<String> names) {
        Set<ArtifactLink> links = new LinkedHashSet<>();
        for (ChainStageResult stage : resultsSummary.getStageResults()) {
            for (BuildResultsSummary build : stage.getBuildResults()) {
                if (!build.getImmutablePlan().getBuildDefinition().getTaskDefinitions().stream().anyMatch(t -> t.getPluginKey().equals(PLUGIN_KEY))) {
                    continue;
                }
                links.addAll(findAllArtifacts(build, names));
            }
        }

        return links;
    }

    public static Set<ArtifactLink> findAllArtifacts(BuildResultsSummary resultsSummary, Set<String> names) {
        return resultsSummary.getArtifactLinks().stream().filter(a -> names.contains(a.getArtifact().getLabel())).collect(Collectors.toSet());
    }
}
