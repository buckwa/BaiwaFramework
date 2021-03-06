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
var ng2_file_upload_1 = require('ng2-file-upload');
var platform_browser_1 = require('@angular/platform-browser');
var Rx_1 = require('rxjs/Rx');
var router_1 = require('@angular/router');
var URL1 = '../person/importwork_file';
var AcademicWork = (function () {
    function AcademicWork(router, commonService, http, sanitizer) {
        this.router = router;
        this.commonService = commonService;
        this.http = http;
        this.sanitizer = sanitizer;
        this.kpiuserList = [];
        this.uploader = new ng2_file_upload_1.FileUploader({ url: URL1 });
        this.academy = this.setdefualtkpi();
        this.kpiuserList = [];
        this.kpival = [];
        this.pointKPI = this.setdefualtpoitkpi();
    }
    AcademicWork.prototype.setdefualtkpi = function () {
        return {
            "academicYear": "",
            "totalInMapping": "",
            "calResultStr": "",
            "pBPWorkTypeList": [{
                    "name": "",
                    "totalInWorkType": "",
                    "academicKPIUserMappingList": [{}]
                }]
        };
    };
    AcademicWork.prototype.setdefualtpoitkpi = function () {
        return {
            "name": "",
            "kpiUserMappingId": "",
            "calResultStr": "",
            "academicKPI": {
                "unitDesc": ""
            },
            "academicKPIAttributeValueList": [{}]
        };
    };
    AcademicWork.prototype.ngOnInit = function () {
        var _this = this;
        console.log("5555");
        this.GetUserSession();
        this.uploader.onBuildItemForm = function (fileItem, form) {
            form.append('academicKPIId', _this.codeKpi);
        };
    };
    AcademicWork.prototype.ngAfterViewInit = function () {
    };
    AcademicWork.prototype.GetUserSession = function () {
        var _this = this;
        var url = "../person/getUserSession";
        return this.http.get(url).subscribe(function (response) { return _this.GetUserSessionSucess(response); }, function (error) { return _this.GetUserSessionError(error); }, function () { return console.log("editdoneUser !"); });
    };
    AcademicWork.prototype.GetUserSessionSucess = function (response) {
        var _this = this;
        this.user = response.json(JSON.stringify(response._body));
        this.academicYearList = this.user.academicYearList;
        this.currentAcademicYear = this.user.currentAcademicYear;
        this.GetPersonByAcadamy(this.user.userName, this.user.currentAcademicYear);
        setTimeout(function () { return _this.GetAcademicWork(_this.user.userName, _this.currentAcademicYear, _this.evaluateRoundValue); }, 250);
    };
    AcademicWork.prototype.GetUserSessionError = function (error) {
        console.log("GetPersonError.");
    };
    AcademicWork.prototype.GetAcademicWork = function (user, year, round) {
        var _this = this;
        this.commonService.loading();
        var url = "../person/getAcademicWork/" + user + "/" + year + "/" + round;
        return this.http.get(url).subscribe(function (response) { return _this.GetUserAcademicSucess(response); }, function (error) { return _this.GetUserSessionError(error); }, function () { return console.log("editdoneUser !"); });
    };
    AcademicWork.prototype.GetUserAcademicSucess = function (response) {
        this.academy = response.json(JSON.stringify(response._body));
        this.academyList = this.academy.pBPWorkTypeList;
        console.log("  academyList ", this.academyList);
        //this.kpiuserList =this.academy.pBPWorkTypeList.academicKPIUserMappingList;
        this.kpiuserList = [];
        for (var i = 0; i < this.academy.pBPWorkTypeList.length; i++) {
            this.kpiuserList.push(this.academy.pBPWorkTypeList[i].academicKPIUserMappingList);
        }
        console.log("this.kpiuserList", this.kpiuserList);
        this.commonService.unLoading();
        this.mapKpi();
    };
    AcademicWork.prototype.ClickGetPointKPI = function (Code, mark) {
        var _this = this;
        this.mark = mark;
        this.codeKpi = Code;
        var url = "../person/getImportWorkNew/" + Code;
        return this.http.get(url).subscribe(function (response) { return _this.GetKPISucess(response); }, function (error) { return _this.GetUserSessionError(error); }, function () { return console.log("editdoneUser !"); });
    };
    AcademicWork.prototype.GetKPISucess = function (response) {
        this.Model = response.json(JSON.stringify(response._body));
        this.pointKPI = this.Model.academicKPIUserMapping;
        this.pointLPIList = this.pointKPI.academicKPIAttributeValueList;
        console.log("this.pointLPIList", this.pointLPIList);
        this.fileWork = this.pointKPI.academicKPIAttachFileList;
        if (this.fileWork.length == 0) {
            this.chFilework = true;
        }
        else {
            this.chFilework = false;
        }
        // FileUploader.prototype.addToQueue(this.f,null,null);
        if (this.pointKPI.status == "APPROVED") {
            this.statusKpi = true;
        }
        else {
            this.statusKpi = false;
        }
        if (this.pointKPI.messageList != null && this.pointKPI.status != "APPROVED") {
            this.messageList = this.pointKPI.messageList;
        }
    };
    AcademicWork.prototype.mapKpi = function () {
        for (var i = 0; i < this.kpiuserList.length; i++) {
            this.kpival[i] = [];
            for (var j = 0; j < this.kpiuserList[i].length; j++) {
                for (var k = 0; k < this.kpiuserList[i][j].academicKPIAttributeValueList.length; k++) {
                    if (this.kpiuserList[i][j].academicKPIAttributeValueList[k].name == 'สัดส่วน(%)') {
                        var temp = this.kpiuserList[i][j].academicKPIAttributeValueList[k].value;
                        this.kpival[i][j] = temp + "%";
                    }
                }
            }
        }
    };
    AcademicWork.prototype.getImage = function (url) {
        return Rx_1.Observable.create(function (observer) {
            var req = new XMLHttpRequest();
            req.open('get', url);
            req.responseType = "arraybuffer";
            req.onreadystatechange = function () {
                if (req.readyState == 4 && req.status == 200) {
                    observer.next(req.response);
                    observer.complete();
                }
            };
            req.send();
        });
    };
    AcademicWork.prototype.getFile = function (KpiID) {
        var _this = this;
        // var data = {'profileImg' : profileImg}
        var url = "../person/getAcademicWork_File/" + KpiID;
        this.getImage(url).subscribe(function (imageData) {
            _this.f = imageData;
            console.log("imageReturn :" + imageData.length);
            //var blob: Blob = new Blob(imageData, JSON.stringify('_body'));
            _this.tmpUrl = URL.createObjectURL(new Blob([imageData]));
            _this.fielPath = _this.sanitize(_this.tmpUrl);
            //this.f = new File(new Blob([imageData]),"name.txt",{type: "image/png"});
            //FileUploader.addToQueue();
            console.log("file");
        });
        // the below will throw not implemented error
        this.http.get(url).subscribe(function (image) {
            console.log("imageUrl :" + image.url);
            console.log(image.arrayBuffer());
        });
    };
    AcademicWork.prototype.sanitize = function (url) {
        return this.sanitizer.bypassSecurityTrustUrl(url);
    };
    AcademicWork.prototype.uploadFileAll = function () {
        var _this = this;
        this.uploader.uploadAll();
        this.uploader.onCompleteItem = function (item, response, status, headers) {
            console.log("ImageUpload:uploaded:", item, status);
            console.log("CodeKpi", _this.codeKpi);
            _this.ClickGetPointKPI(_this.codeKpi, _this.mark);
        };
        // window.setTimeout(() => {
        //     var temp = !this.uploader.getNotUploadedItems().length;
        // }, 900);
        // console.log(this.uploader);
        // console.log(this.uploader.getNotUploadedItems());
    };
    AcademicWork.prototype.deleteFile = function (attachFileId, fileName) {
        var url = "../person/deleteAttachFile/" + this.codeKpi + "/" + fileName + "/" + attachFileId;
        this.commonService.confirm("คุณต้องการลบเอกสารแบบใช่หรื่อไม่?", jQuery.proxy(function (isOk) {
            var _this = this;
            console.log("isOk", isOk);
            if (isOk) {
                this.http.get(url).subscribe(function (response) { return _this.deletesucess(response); }, function (error) { return _this.deleteError(); }, function () { return console.log("editdoneUser !"); });
            }
        }, this));
    };
    AcademicWork.prototype.deletesucess = function (response) {
        console.log("deletesucess!");
        this.ClickGetPointKPI(this.codeKpi, this.mark);
    };
    AcademicWork.prototype.deleteError = function () {
        console.log("deleteError!");
    };
    AcademicWork.prototype.GetPersonByAcadamy = function (user, year) {
        var _this = this;
        var url = "../person/getPersonByAcademicYear/" + user + "/" + year;
        this.http.get(url).subscribe(function (response) { return _this.GetPersonSucess(response); }, function (error) { return _this.GetPersonError(error); }, function () { return console.log("editdone !"); });
    };
    AcademicWork.prototype.GetPersonSucess = function (response) {
        this.profile = response.json(JSON.stringify(response._body));
        this.evaluateRoundValue = this.profile.evaluateRound;
        if (this.profile.employeeType == 'ข้าราชการ') {
            this.evaluateRoundList = this.profile.evaluateRoundList;
        }
    };
    AcademicWork.prototype.GetPersonError = function (error) {
        console.log("GetPersonError.");
    };
    AcademicWork.prototype.changeYear = function (year) {
        this.GetAcademicWork(this.user.userName, year, this.evaluateRoundValue);
    };
    AcademicWork.prototype.changeRound = function () {
        this.GetAcademicWork(this.user.userName, this.currentAcademicYear, this.evaluateRoundValue);
    };
    AcademicWork.prototype.sentReplyPBPMessage = function () {
        var _this = this;
        if (this.replyMessage != null) {
            this.Model.replyMessage = this.replyMessage;
            var url = "../head/pbp/replyMessage"; //ติดไว้ก่อน
            this.http.post(url, this.Model).subscribe(function (response) { return _this.ReplyPBPMessageSucess(response); }, function (error) { return _this.ReplyPBPMessageError(error); }, function () { return console.log("AdminUserCreate : Success saveUser !"); });
        }
    };
    AcademicWork.prototype.ReplyPBPMessageSucess = function (response) {
        var temp = response.json(JSON.stringify(response._body));
        this.ClickGetPointKPINew(this.codeKpi, this.mark);
    };
    AcademicWork.prototype.ReplyPBPMessageError = function (response) {
        console.log("Error!");
    };
    AcademicWork.prototype.ClickGetPointKPINew = function (Code, indexKPI) {
        var _this = this;
        this.mark = indexKPI;
        var url = "../person/getImportWorkNew/" + Code;
        return this.http.get(url).subscribe(function (response) { return _this.GetKPISucessNew(response); }, function (error) { return _this.ReplyPBPMessageError(error); }, function () { return console.log("editdoneUser !"); });
    };
    AcademicWork.prototype.GetKPISucessNew = function (response) {
        this.Model = response.json(JSON.stringify(response._body));
        this.pointKPI = this.Model.academicKPIUserMapping;
        if (this.pointKPI.messageList != null) {
            this.messageList = this.pointKPI.messageList;
        }
    };
    AcademicWork.prototype.clickedDeteleImport = function () {
        this.commonService.confirm("Are you sure you want to delete?", jQuery.proxy(function (isOk) {
            var _this = this;
            if (isOk) {
                //action 
                var url = "../pam/person/deleteImportWork/" + this.codeKpi;
                return this.http.get(url).subscribe(function (response) { return _this.GetSSDeteleImport(response); }, function (error) { return _this.GetERRDeteleImport(error); }, function () { return console.log("editdoneUser !"); });
            }
        }, this));
    };
    AcademicWork.prototype.GetSSDeteleImport = function (response) {
        var _this = this;
        this.result = response.json(JSON.stringify(response._body));
        location.reload();
        setTimeout(function () { return _this.GetAcademicWork(_this.user.userName, _this.currentAcademicYear, _this.evaluateRoundValue); }, 250);
    };
    AcademicWork.prototype.GetERRDeteleImport = function (response) {
        console.log("Error!");
    };
    AcademicWork.prototype.clickedEditImport = function () {
        var _this = this;
        var keys = Object.keys(this.pointLPIList);
        var len = keys.length;
        var tamp = 1;
        for (var i = 0; i < len; i++) {
            if (this.pointLPIList[i].value == null) {
                console.log("Required Now !");
                tamp = 0;
            }
            if (this.pointLPIList[i].name == 'สัดส่วน(%)') {
                if (this.pointLPIList[i].value > 100) {
                    console.log("Number limit !");
                    tamp = 0;
                }
            }
        }
        if (tamp == 1) {
            this.Model.academicKPIAttributeValueList = this.pointLPIList;
            var url = "../pam/person/editImportwork";
            this.http.post(url, this.Model).subscribe(function (response) { return _this.EditSuccess(response); }, function (error) { return _this.EditError(error); }, function () { return console.log("AdminUserCreate : Success saveUser !"); });
        }
    };
    AcademicWork.prototype.EditSuccess = function (response) {
        var _this = this;
        this.result = response.json(JSON.stringify(response._body));
        if (this.result.status == 'S001') {
            alert("Success !");
            setTimeout(function () { return _this.GetAcademicWork(_this.user.userName, _this.currentAcademicYear, _this.evaluateRoundValue); }, 250);
        }
        else {
            alert("Error !");
        }
    };
    AcademicWork.prototype.EditError = function (response) {
        alert("Error !");
    };
    AcademicWork.prototype.exitModal = function () {
        this.uploader.clearQueue();
    };
    AcademicWork = __decorate([
        core_1.Component({
            templateUrl: 'app/baiwa/html/AcademicWork.component.html'
        }), 
        __metadata('design:paramtypes', [router_1.Router, Common_service_1.CommonService, http_1.Http, platform_browser_1.DomSanitizer])
    ], AcademicWork);
    return AcademicWork;
}());
exports.AcademicWork = AcademicWork;
//# sourceMappingURL=AcademicWork.component.js.map