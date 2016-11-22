"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var Common_service_1 = require('./../service/Common.service');
var http_1 = require('@angular/http');
var InitApporve = (function () {
    function InitApporve(commonService, http) {
        this.commonService = commonService;
        this.http = http;
        this.tabPerson = false;
    }
    InitApporve.prototype.ngOnInit = function () {
        this.initTotle();
    };
    InitApporve.prototype.initTotle = function () {
        var _this = this;
        var url = "../head/init";
        this.http.get(url).subscribe(function (response) { return _this.initTotleSucess(response); }, function (error) { return _this.initTotlError(error); }, function () { return console.log("editdoneUser !"); });
    };
    InitApporve.prototype.initTotleSucess = function (response) {
        var bodyJson;
        bodyJson = response.json(JSON.stringify(response._body));
        this.academicPersonList = bodyJson.department.academicPersonList;
    };
    InitApporve.prototype.initTotlError = function (error) {
        console.log("error getTotle");
    };
    InitApporve.prototype.clickPerson = function (index) {
        this.tabPerson = true;
        this.personWork = this.academicPersonList[index];
        console.log("index of personlist is " + index);
        this.personWorkList = this.personWork.academicKPIUserMappingList;
    };
    InitApporve.prototype.blackpage = function () {
        this.tabPerson = false;
    };
    InitApporve = __decorate([
        core_1.Component({
            templateUrl: 'app/baiwa/html/initApprove.component.html'
        }), 
        __metadata('design:paramtypes', [Common_service_1.CommonService, http_1.Http])
    ], InitApporve);
    return InitApporve;
}());
exports.InitApporve = InitApporve;
//# sourceMappingURL=initApprove.component.js.map