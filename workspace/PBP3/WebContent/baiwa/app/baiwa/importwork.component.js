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
var ng2_file_upload_1 = require('ng2-file-upload');
var URL = 'http://localhost:8080/PBP3/person/uploadMultiFile';
// class FileSelectDirective
var importwork = (function () {
    // 555+
    function importwork() {
        this.uploader = new ng2_file_upload_1.FileUploader({ url: URL });
        console.log('55+');
        //this.uploader.kpiUserMappingId='3333';
    }
    importwork.prototype.ngOnInit = function () {
        this.uploader.onBeforeUploadItem = function (fileItem) {
            fileItem.formData.push({ kpiUserMappingId: '33' });
        };
    };
    importwork.prototype.ngAfterViewInit = function () {
    };
    importwork = __decorate([
        core_1.Directive({ selector: '[ng2FileSelect]' }),
        core_1.Directive({ selector: '[ng2FileDrop]' }),
        core_1.Component({
            templateUrl: 'app/baiwa/html/importwork.component.html'
        }), 
        __metadata('design:paramtypes', [])
    ], importwork);
    return importwork;
}());
exports.importwork = importwork;
//# sourceMappingURL=importwork.component.js.map