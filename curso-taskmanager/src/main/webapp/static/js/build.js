({
    //    baseUrl: ".",
    //    name: "vendor/almond",
    //    include: "require_main",
    //    mainConfigFile: "require_main.js",
    //    out: "@outputFile@",
    //    optimize: "none",
    wrapShim: true,
    findNestedDependencies: true,
    baseUrl: ".",
    name: "vendor/almond",
    include: "require_main",
    mainConfigFile: "require_main.js",
    out: "@outputFile@",
    exclude: ["babel"],
    optimize: "none",
    pragmasOnSave: {
        excludeBabel: true
    }
});
