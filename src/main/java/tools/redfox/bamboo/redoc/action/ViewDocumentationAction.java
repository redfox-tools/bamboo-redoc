package tools.redfox.bamboo.redoc.action;

import com.atlassian.bamboo.build.PlanResultsAction;
import com.atlassian.bamboo.build.artifact.ArtifactLink;
import com.atlassian.bamboo.build.artifact.ArtifactLinkManager;
import com.atlassian.bamboo.chains.BuildContextFactory;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.jsonator.DefaultJsonator;
import com.atlassian.bamboo.plan.PlanExecutionManager;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.cache.CachedPlanManager;
import com.atlassian.bamboo.security.BambooPermissionManager;
import com.atlassian.bamboo.user.BambooAuthenticationContext;
import com.atlassian.bamboo.vcs.configuration.service.VcsRepositoryConfigurationService;
import com.atlassian.bamboo.ww2.aware.permissions.PlanReadSecurityAware;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.WebInterfaceManager;
import org.apache.struts2.ServletActionContext;
import tools.redfox.bamboo.redoc.helper.ReDocHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

import static tools.redfox.bamboo.redoc.helper.ReDocHelper.findReDocArtifact;

public class ViewDocumentationAction extends PlanResultsAction implements PlanReadSecurityAware {
    public ViewDocumentationAction(
            @ComponentImport AdministrationConfigurationAccessor administrationConfigurationAccessor,
            @ComponentImport ArtifactLinkManager artifactLinkManager,
            @ComponentImport WebInterfaceManager webInterfaceManager,
            @ComponentImport BambooAuthenticationContext authenticationContext,
            @ComponentImport BambooPermissionManager bambooPermissionManager,
            @ComponentImport PlanExecutionManager planExecutionManager,
            @ComponentImport PlanManager planManager,
            @ComponentImport CachedPlanManager cachedPlanManager,
            @ComponentImport VcsRepositoryConfigurationService vcsRepositoryConfigurationService,
            @ComponentImport BuildContextFactory buildContextFactory
    ) {
        setAdministrationConfigurationAccessor(administrationConfigurationAccessor);
        setArtifactLinkManager(artifactLinkManager);
        setWebInterfaceManager(webInterfaceManager);
        setBambooPermissionManager(bambooPermissionManager);
        setWebInterfaceManager(webInterfaceManager);
        setAdministrationConfigurationAccessor(administrationConfigurationAccessor);
        setAuthenticationContext(authenticationContext);
        setJsonator(new DefaultJsonator());
        setBambooPermissionManager(bambooPermissionManager);
        setPlanExecutionManager(planExecutionManager);
        setPlanManager(planManager);
        setCachedPlanManager(cachedPlanManager);
        setVcsRepositoryConfigurationService(vcsRepositoryConfigurationService);
        setBuildContextFactory(buildContextFactory);
    }

    @Override
    public String execute() throws Exception {
        Set<String> artifactNames = ReDocHelper.getAvailableArtifactNames(getResultsSummary());
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, Object> context = ServletActionContext.getValueStack(request).getContext();

        ArtifactLink link = findReDocArtifact(getResultsSummary().getArtifactLinks(), artifactNames);
        context.put("link", link);

        return super.execute();
    }
}
