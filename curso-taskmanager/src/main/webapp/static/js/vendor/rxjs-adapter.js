define([
  'rxjs'
], function (
  Rx
) {
  const definitions = {
    'rxjs/Observable': Rx,
    'rxjs/Subject': Rx,
    'rxjs/operator/filter': Rx.Observable.prototype,
    'rxjs/operator/map': Rx.Observable.prototype,
    'rxjs/operator/switchMap': Rx.Observable.prototype,
    'rxjs/observable/from': Rx.Observable,
    'rxjs/observable/merge': Rx.Observable,
    'rxjs/observable/of': Rx.Observable
  };
  Object.keys(definitions).forEach(function (definition) {
    const object = definitions[definition];
    define(definition, function () {
      return object;
    });
  });
  return Rx;
});