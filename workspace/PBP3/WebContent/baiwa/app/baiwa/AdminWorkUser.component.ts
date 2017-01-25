import { Component, OnInit, ViewChild, AfterViewInit, HostListener } from '@angular/core';
import {CommonService} from './../service/Common.service';
import { Http, Headers, Response } from '@angular/http';
import { Router, ActivatedRoute, NavigationCancel  } from '@angular/router';


declare var jQuery: any;


@Component({
    templateUrl: 'app/baiwa/html/AdminWorkUser.component.html'
})
export class AdminWorkUser implements OnInit {

    @ViewChild('personTimeTable') timetabletable;

    @ViewChild('name') name: any;

    public data: any;
    public datalist: any;

    public makeDataTable: any = {
        "searching": true,
        "bPaginate": false,
        "paging": true,
        "bLengthChange": false,
        "bInfo": false,
        "bAutoWidth": false,
        "columns": [
            { "data": "username" },
            { "data": "firstLastName" },
            { "data": "facultyDesc" },
            { "data": "departmentDesc" },
            { "data": "employeeType" },
            { "data": "academicYear" },
            {
                data: "username",
                className: "center",
                "render": function (data, type, full, meta) {
                    return '<a href="javascript:void(0);"  Action="Edit" value="' + data + '" class="editor_remove">Edit</a> / <a href="javascript:void(0);" Action="Edit" class="editor_remove">Delete</a>';
                }
            }

        ]
    };


    constructor(private router: Router, private commonService: CommonService, private http: Http) {

    }
    ngOnInit() {
        this.getDatatabel1();

    }
    public getDatatabel1() {
        var url = "../admin/json/GetUserlist";
        this.http.get(url).subscribe(response => this.getTimeTableSucess(response),
            error => this.GetPersonError(error), () => console.log("callsevice done !"));

    }
    getTimeTableSucess(response: any) {
        this.datalist = response.json(JSON.stringify(response._body));
        this.makeDataTable.data = this.datalist[0].currentPageItem;
        this.timetabletable.show();

    }
    GetPersonError(error: any) {
        console.log("call service error" + error);

    }

    @HostListener('click', ['$event.target'])
    handleKeyboardEvent(target: any) {
        //
        let ele = jQuery(target);
        console.log(ele.attr("value"), ele.attr("Action"));
        if (ele.attr("Action") == "Edit") {
            if (ele.attr("value")) {
                this.router.navigate(['/AdminUserEdit', ele.attr("value")]);
            }
        }
    }

    DeleteDataUser(Username: any) {


    }

    UpdateDataUser(Username: any) {


    }


}