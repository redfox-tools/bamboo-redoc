[#-- @ftlvariable name="action" type="com.atlassian.bamboo.ww2.actions.chains.ArtifactUrlRedirectAction" --]
[#-- @ftlvariable name="" type="com.atlassian.bamboo.ww2.actions.chains.ArtifactUrlRedirectAction" --]

<html>
<head>
    <title>[@s.text name="Documentation"/]</title>
    <meta name="tab" content="redoc"/>
</head>

<body>
<style>
    .aui-page-panel-content {
        padding: 0px;
    }
</style>
<iframe src="${action.getArtifactLinkUrl(link)}" frameborder="0" width="100%" height="1200px"></iframe>

</body>
</html>

