//
//  ViewController.swift
//  login
//
//  Created by Student09 on 2017/11/17.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    
    var codeEdit: UITextField!
    var mobileEdit:UITextField!
    var pwdEdit:UITextField!
    
    var result: Int!
    
    @objc func forCode() {
        result = Int(arc4random()) % (9999 - 1000 + 1) + 1000
        print(result)
    }
    
    @objc func forRegister() {
        //实现页面的跳转
        let myInt = Int(codeEdit.text!)
        //print(myInt!)
        if  myInt == result{
            
            //将用户信息存储plist文件中
            let dic:NSMutableDictionary = NSMutableDictionary()
            let mobile = mobileEdit.text
            let pwd = pwdEdit.text
            dic.setObject(mobile!, forKey: "userID" as NSCopying)
            dic.setObject(pwd!, forKey: "password" as NSCopying)
            
            let plistPath = Bundle.main.path(forResource: "userInformation", ofType: "plist")
            dic.write(toFile: plistPath!, atomically: true)
            
            let data:NSMutableDictionary = NSMutableDictionary.init(contentsOfFile:plistPath!)!
            let message = data.description
            
            print(plistPath!)
            print(message)
            
            let registerViewController = RegisterViewController()
            registerViewController.viewController = self
            self.present(registerViewController, animated: true, completion: nil)
        }
        else {
            print("验证码错误，请重新输入")
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.white
        // Do any additional setup after loading the view, typically from a nib.
        let title = UILabel(frame: CGRect(x: 130, y: 60, width: 70, height: 50))
        title.text = "注册"
        title.font = UIFont.systemFont(ofSize: 25)
        self.view.addSubview(title)
        
        let mobile = UILabel(frame: CGRect(x: 20, y: 150, width: 70, height: 50))
        mobile.text = "手机号码"
        mobileEdit = UITextField(frame: CGRect(x: 100, y: 150, width: 210, height: 50))
        mobileEdit.placeholder = "请输入您的手机号码"
        self.view.addSubview(mobile)
        self.view.addSubview(mobileEdit)
        
        let code = UILabel(frame: CGRect(x: 20, y: 200, width: 70, height: 50))
        code.text = "验证码"
        codeEdit = UITextField(frame: CGRect(x: 100, y: 200, width: 120, height: 50))
        codeEdit.placeholder = "请输入验证码"
        codeEdit.keyboardType = UIKeyboardType.default

        let pwd = UILabel(frame: CGRect(x: 20, y: 250, width: 70, height: 50))
        pwd.text = "密码"
        pwdEdit = UITextField(frame: CGRect(x: 100, y: 250, width: 210, height: 50))
        pwdEdit.placeholder = "请输入您要设置的密码"
        pwdEdit.isSecureTextEntry = true
        self.view.addSubview(pwd)
        self.view.addSubview(pwdEdit)
        
        
        let getCode = UIButton(frame: CGRect(x: 220, y: 210, width: 70, height: 30))
        getCode.backgroundColor = UIColor.lightGray
        getCode.layer.borderWidth = 2;
        getCode.layer.cornerRadius = 10;
        getCode.setTitle("获取验证码", for:.normal)
        getCode.titleLabel?.font = UIFont.systemFont(ofSize: 11)
        
        //添加获取验证码的点击事件
        getCode.addTarget(self,action:#selector(ViewController.forCode),for:UIControlEvents.touchUpInside)
        
        let register = UIButton(frame: CGRect(x: 110, y: 350, width: 100, height: 40))
        register.setTitle("注册", for: UIControlState())
        register.backgroundColor = UIColor.black
        //添加注册按钮的点击事件
        register.addTarget(self,action:#selector(ViewController.forRegister),for:UIControlEvents.touchUpInside)
        
        self.view.addSubview(code)
        self.view.addSubview(codeEdit)
        self.view.addSubview(getCode)
        self.view.addSubview(register)
        
        
        func didReceiveMemoryWarning() {
            super.didReceiveMemoryWarning()
            // Dispose of any resources that can be recreated.
        }

    }
    
}

