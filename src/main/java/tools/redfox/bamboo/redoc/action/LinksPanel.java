package tools.redfox.bamboo.redoc.action;

import com.atlassian.bamboo.build.artifact.ArtifactLink;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.template.TemplateRenderer;
import com.atlassian.bamboo.ww2.actions.chains.ViewChainResult;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.model.WebPanel;
import tools.redfox.bamboo.redoc.helper.ReDocHelper;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static tools.redfox.bamboo.redoc.helper.ReDocHelper.findAllArtifacts;

public class LinksPanel implements WebPanel {
    private TemplateRenderer templateRenderer;

    public LinksPanel(@ComponentImport TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public String getHtml(Map<String, Object> context) {
        ChainResultsSummary result = (ChainResultsSummary) context.get("resultSummary");
        ViewChainResult action = (ViewChainResult) context.get("action");
        Set<String> artifactNames = ReDocHelper.getAvailableArtifactNames(result);
        Map<String, String> links = new HashMap<>();

        for (ArtifactLink link : findAllArtifacts(result, artifactNames)) {
            links.put(link.getArtifact().getLabel(), action.getArtifactLinkUrl(link));
        }

        context.put("redoc", links);

        return templateRenderer.renderWithoutActionContext("/templates/tools/redfox/redoc/links.ftl", context);
    }

    @Override
    public void writeHtml(Writer writer, Map<String, Object> context) throws IOException {
        writer.write(getHtml(context));
    }
}
