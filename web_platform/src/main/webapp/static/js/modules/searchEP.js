/**
 * Created by Zale on 6/20/2014.
 */
define(['knockout', 'paramsService', 'epService'], function (ko, paramsService, epService) {

  var SearchEPModel = function (id, title, role_type, selectedFunction) {
    var me = this;
    this.id = ko.observable(id);
    this.title = ko.observable(title);
    this.roleType = role_type;
    this.provinceOptions = ko.observableArray([]);
    this.cityOptions = ko.observableArray([]);
    this.areaOptions = ko.observableArray([]);
    this.province = ko.observable();
    this.city = ko.observable();
    this.area = ko.observable();
    this.name = ko.observable();


    this.results = ko.observableArray([]);
    this.searchLoading = ko.observable(false);
    this.selectedFunction = selectedFunction;
    this.init = function () {
      me.province('');
      me.city('');
      me.area('');
      me.name('');
      me.results([]);
    };
    this.province.subscribe(function (newValue) {
      if (typeof newValue != 'undefined'&&newValue!='') {
        me.loadCity(newValue);
      }
    });

    this.city.subscribe(function (newValue) {
      if (typeof newValue != 'undefined'&&newValue!='') {
        me.loadArea(newValue);
      }
    });

    this.loadProvince = function () {
      var deferred = paramsService.getParams('LocationArea', '0');
      $.when(deferred).done(function (response) {

        if (response.code == 200 && response.result.length > 0) {
          me.provinceOptions(response.result);
        } else {
          console.log('load province failed');
        }
      }).fail(function (error) {
        console.log(error);
      });
    };

    this.loadCity = function (provinceId) {
      me.cityOptions([]);
      var deferred = paramsService.getParams('LocationArea', provinceId);
      $.when(deferred).done(function (response) {
        if (response.code == 200 && response.result.length > 0) {
          me.cityOptions(response.result);
        } else {
          console.log('load city failed');
        }
      }).fail(function (error) {
        console.log(error);
      });
    };
    this.loadArea = function (cityId) {
      me.areaOptions([]);
      var deferred = paramsService.getParams('LocationArea', cityId);
      $.when(deferred).done(function (response) {
        if (response.code == 200 && response.result.length > 0) {
          me.areaOptions(response.result);
        } else {
          console.log('load city failed');
        }
      }).fail(function (error) {
        console.log(error);
      });
    };
    this.loadEPNames = function () {
      var payload = {name: me.name(), role_type: me.roleType, province: me.province(), city: me.city(), area: me.area()};
      var deferred = epService.getEnterpriseNamesBy(payload);
      $.when(deferred).done(function (response) {
        if (response.code == 200 && response.result.length > 0) {
          me.results(response.result);
          me.searchLoading(false);
        } else {
          console.log('load status failed');
          me.searchLoading(false);
        }
      }).fail(function (error) {
        console.log(error);
        me.searchLoading(false);
      });
    };

    this.afterSelected = function (element) {
      var selected = $(element);
      me.init();
      me.selectedFunction(element.id, selected.text());
    }
    this.search = function () {
      me.searchLoading(true);
      me.loadEPNames()
    }



    me.loadProvince();
  };
  return SearchEPModel;
});