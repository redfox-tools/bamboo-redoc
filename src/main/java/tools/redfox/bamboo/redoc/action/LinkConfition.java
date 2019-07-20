package tools.redfox.bamboo.redoc.action;

import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.Condition;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static tools.redfox.bamboo.redoc.action.ViewDocumentationAction.findReDocArtifact;

public class LinkConfition implements Condition {
    private ResultsSummaryManager resultsSummaryManager;

    public LinkConfition(@ComponentImport ResultsSummaryManager resultsSummaryManager) {
        this.resultsSummaryManager = resultsSummaryManager;
    }

    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> context) {
        String buildKey = StringUtils.defaultString((String) context.get("planKey"), (String) context.get("buildKey"));
        String buildNumberString = (String) context.get("buildNumber");
        if (buildKey != null && buildNumberString != null) {
            try {
                int buildNumber = Integer.parseInt(buildNumberString);
                ResultsSummary buildResults = this.resultsSummaryManager.getResultsSummary(PlanKeys.getPlanResultKey(buildKey, buildNumber));
                if (buildResults == null || !buildResults.isFinished()) {
                    return false;
                }
                return findReDocArtifact(buildResults.getArtifactLinks()) != null;
            } catch (Exception var6) {
            }
        }

        return false;
    }
}
