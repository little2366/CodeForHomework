//
//  LoginViewController.swift
//  login
//
//  Created by Student09 on 2017/11/22.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {
    
    var viewController : RegisterViewController?
    
    var pwdEdit:UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.white
        let title = UILabel(frame: CGRect(x: 130, y: 30, width: 70, height: 50))
        title.text = "登录"
        title.font = UIFont.systemFont(ofSize: 25)
        self.view.addSubview(title)
        
        let id = UILabel(frame: CGRect(x: 20, y: 150, width: 70, height: 50))
        id.text = "手机号码"
        let idEdit = UITextField(frame: CGRect(x: 100, y: 150, width: 210, height: 50))
        idEdit.placeholder = "请输入您的手机号码"
        self.view.addSubview(id)
        self.view.addSubview(idEdit)
        
        let pwd = UILabel(frame: CGRect(x: 20, y:200, width: 70, height: 50))
        pwd.text = "密码"
        pwdEdit = UITextField(frame: CGRect(x: 100, y: 200, width: 210, height: 50))
        pwdEdit.placeholder = "请输入您的密码"
        pwdEdit.isSecureTextEntry = true
        self.view.addSubview(pwd)
        self.view.addSubview(pwdEdit)
        
        
        
        let login = UIButton(frame: CGRect(x: 100, y: 300, width: 100, height: 44))
        login.setTitle("登录", for: UIControlState())
        login.backgroundColor = UIColor.black
        login.addTarget(self, action: #selector(LoginViewController.dismissSelf), for: .touchUpInside)
        self.view.addSubview(login)
        // Do any additional setup after loading the view.
    }
    
    @objc func dismissSelf()
    {
        //比较与注册时候的密码是否相同
        let password1 = pwdEdit.text
        print(password1!)
        let plistPath = Bundle.main.path(forResource: "userInformation", ofType: "plist")
        let data:NSMutableDictionary = NSMutableDictionary.init(contentsOfFile:plistPath!)!
        let message = data.description
        let password2 = String(describing: data["password"]!)
        print(password2)
        if password1 == password2 {
            let firstViewController = FirstViewController()
            let secondViewController = SecondViewController()
            let thirdViewController = ThirdViewController()
            
            let tabViewController = UITabBarController()
            tabViewController.viewControllers = [firstViewController, secondViewController, thirdViewController]
            
            let tabBar = tabViewController.tabBar
            let item1 = tabBar.items![0]
            item1.title = "课堂"
            item1.image = UIImage(named: "Tab1")
            
            let item2 = tabBar.items![1]
            item2.title = "消息"
            item2.badgeValue = "8"
            item2.image = UIImage(named: "Tab2")
            
            let item3 = tabBar.items![2]
            item3.title = "我的"
            item3.image = UIImage(named: "Tab3")
            
            
            //let newViewController = tabViewController
            self.present(tabViewController, animated: true, completion: nil)
        }
        else {
            print("账号与密码不一致，请重新输入")
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

