//
//  SchoolViewController.swift
//  login
//
//  Created by Student09 on 2017/12/1.
//  Copyright © 2017年 s. All rights reserved.
//

//
//  LoginViewController.swift
//  login
//
//  Created by Student09 on 2017/11/22.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class SchoolViewController: UIViewController {
    
    var viewController : RegisterViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.white
        
        let school = UILabel(frame: CGRect(x: 20, y: 400, width: 70, height: 50))
        school.text = "学校"
        let schoolEdit = UITextField(frame: CGRect(x: 100, y: 400, width: 210, height: 50))
        schoolEdit.placeholder = "请选择您的学校"
        self.view.addSubview(school)
        self.view.addSubview(schoolEdit);
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}


