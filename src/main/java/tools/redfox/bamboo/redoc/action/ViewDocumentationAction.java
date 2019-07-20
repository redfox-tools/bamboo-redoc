package tools.redfox.bamboo.redoc.action;

import com.atlassian.bamboo.build.PlanResultsAction;
import com.atlassian.bamboo.build.artifact.ArtifactLink;
import com.atlassian.bamboo.build.artifact.ArtifactLinkManager;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

public class ViewDocumentationAction extends PlanResultsAction {
    public ViewDocumentationAction(@ComponentImport AdministrationConfigurationAccessor administrationConfigurationAccessor, @ComponentImport ArtifactLinkManager artifactLinkManager) {
        setAdministrationConfigurationAccessor(administrationConfigurationAccessor);
        setArtifactLinkManager(artifactLinkManager);
    }

    @Override
    public String execute() throws Exception {
        ArtifactLink link = findReDocArtifact(resultsSummary.getArtifactLinks());

        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, Object> context = ServletActionContext.getValueStack(request).getContext();
        context.put("link", link);

        return super.execute();
    }

    public static ArtifactLink findReDocArtifact(Collection<ArtifactLink> artifactLinkCollection) {
        return artifactLinkCollection.stream().filter(a -> a.getArtifact().getLabel().equals("ReDoc Documentation")).findFirst().orElse(null);
    }
}
