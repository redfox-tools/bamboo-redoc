[@ww.textfield labelKey="tools.redfox.redoc.form.spec.label" descriptionKey="tools.redfox.redoc.form.spec.description" name='tools.redfox.redoc.spec' /]
[@ww.textfield labelKey="tools.redfox.redoc.form.options.label" descriptionKey="tools.redfox.redoc.form.options.description" name='tools.redfox.redoc.options' /]

[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='redoc' /][/#assign]
[@ww.select
    cssClass="field-long"
    labelKey="tools.redfox.redoc.form.executable.label"
    descriptionKey="tools.redfox.redoc.form.executable.description"
    name="tools.redfox.redoc.executable"
    list=uiConfig.getExecutableLabels("redoc")
    extraUtility=addExecutableLink /]

[@ui.bambooSection titleKey='repository.advanced.option' collapsible=true isCollapsed=!(environmentVariables?has_content || workingSubDirectory?has_content)]
    [@s.textfield labelKey='builder.common.env' name='tools.redfox.redoc.environmentVariable' cssClass="long-field" /]
    [@s.textfield labelKey='builder.common.sub' name='tools.redfox.redoc.workingDirectory' cssClass="long-field" /]
[/@ui.bambooSection]
