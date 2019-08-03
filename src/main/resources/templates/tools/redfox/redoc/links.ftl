[#-- @ftlvariable name="action" type="com.atlassian.bamboo.ww2.actions.chains.ViewChainResult" --]
[#-- @ftlvariable name="" type="com.atlassian.bamboo.ww2.actions.chains.ViewChainResult" --]
[#if redoc.size() > 0 ]
    <h2>ReDoc Documentations</h2>
    <ul>
    [#list redoc.keySet() as name]
        <li><a href="${redoc.get(name)}">${name}</a></li>
    [/#list]
    </ul>
[/#if]
