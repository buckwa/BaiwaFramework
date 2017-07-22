import { Component, OnInit, ViewChild, AfterViewInit, HostListener  } from '@angular/core';
import {CommonService} from './../service/Common.service';
import { Http, Headers, Response } from '@angular/http';
import { Router, ActivatedRoute, NavigationCancel  } from '@angular/router';

declare var jQuery: any;

@Component({
    templateUrl: 'app/baiwa/html/AdminChainOfCommandinitHead.component.html'
})

export class AdminChainOfCommandinitHead implements OnInit {
   @ViewChild('personTimeTable') timetabletable;

    public president: any;
    public presidents: any;
    public department: any;
    public thainame: any;
    public thaiSurname: any;
    public academicYear: any;
    public resPagingBean: any;
    public currentPageItem: any;
    public head: any;
    public result: any;
    constructor(private router: Router, private commonService: CommonService, private http: Http, private route: ActivatedRoute) {


    }

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
            { "data": "departmentDesc" },
            { "data": "employeeType" },
            { "data": "academicYear" },
            {
                data: "username",
                className: "center",
                "render": function (data, type, full, meta) {
                    return '<a href="javascript:void(0);"  Action="Edit" value="' + data + '" class="editor_remove">กำหนดหัวหน้าภาควิชา</a>';
                }
            }

        ]
    };

    ngOnInit() {

        this.route.params.subscribe(params => this.result = params["result"]);
        if (this.result) {
            console.log("result :", this.result);
            //this.editcar(this.para_carId);
        } 

        this.getDatatabel1();

 
    }

    ngAfterViewInit() {

    }



    public getDatatabel1() {
        var url = "../admin/json/manageHead/"+this.result;
        this.http.get(url).subscribe(response => this.getTimeTableSucess(response),
            error => this.GetPersonError(error), () => console.log("callsevice done !"));
    }

    getTimeTableSucess(response: any) {
        //this.makeDataTable.data = response.json(JSON.stringify(response._body));
        this.presidents = response.json(JSON.stringify(response._body));
        this.president = this.presidents.resObj;
        this.academicYear = this.president.academicYear;
        this.department =this.president.department.name;
        this.head = this.president.head;

        if(this.head!=null){

        this.thainame = this.head.thaiName;
        this.thaiSurname = this.head.thaiSurname;
        }else{
        this.thainame = '';
        this.thaiSurname = '';
        }


        


        this.resPagingBean = this.presidents.resPagingBean;
        this.currentPageItem = this.presidents.resPagingBean.currentPageItem;
        this.makeDataTable.data = this.currentPageItem;
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
                this.getSaveDatatabel1(ele.attr("value"));
                //this.router.navigate(['/AdminUserEdit', ele.attr("value")]);
            }
        }
    }

    public getSaveDatatabel1(response: any) {

        var url = "../admin/json/assignToHead/"+response+"/WORK";
        
        this.http.post(url, this.president).subscribe(response => this.SENT_ss(response),
            error => this.GetPersonError(error), () => console.log(" done !"));

    }

  SENT_ss(response: any) {
    
  
    this.router.navigate(['/AdminChainOfCommandinit']);

  }
}