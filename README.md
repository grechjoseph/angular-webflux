<h1>Spring Web Flux</h1>
<b>com.jg.webflux.direct.FluxController.fluxAllPages</b> simulates publishing 10 pages of Elements with a delay between each.

<h1>Angular Web Client</h1>
<ol>
    <li>ng new web-client</li>
    <li>cd web-client</li>
    <li>npm install sse.js</li>
    <li>ng generate service service/flux</li>
    <li>ng generate component flux</li>
    <li>ng generate class model/element --type=model</li>
</ol>

<b>app.component.html</b> add < app-flux >< /app-flux > to include flux component.