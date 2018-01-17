//
//  RegisterViewController.swift
//  login
//
//  Created by Student09 on 2017/11/22.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class RegisterViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    var flag = 0;
    
    /// 日期输入框
    var birthdayEdit = UITextField()
    /// 日期选择器
    var datePicker = UIDatePicker()
    var uiview = UIView()
    
    var viewController : ViewController?
    
    var countries :Dictionary<String, [String]> = [ "D":["东南大学"], "H":["杭州师范大学","杭州电子科技大学"], "N":["南京大学"],  "Q":["清华大学"], "S":["四川大学","上海交通大学"],"Z":["浙江大学","浙江工商大学","浙江理工大学"]]
    
    var keys:[String] = []
    var schoolEdit = UITextField()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.white
        
        let title = UILabel(frame: CGRect(x: 80, y: 30, width: 180, height: 50))
        title.text = "填写您的个人信息"
        title.font = UIFont.systemFont(ofSize: 18)
        self.view.addSubview(title)
        
        let id = UILabel(frame: CGRect(x: 20, y: 100, width: 70, height: 50))
        id.text = "学号"
        let idEdit = UITextField(frame: CGRect(x: 100, y: 100, width: 210, height: 50))
        idEdit.placeholder = "请输入您的学号"
        self.view.addSubview(id)
        self.view.addSubview(idEdit)
        
        let name = UILabel(frame: CGRect(x: 20, y: 150, width: 70, height: 50))
        name.text = "姓名"
        let nameEdit = UITextField(frame: CGRect(x: 100, y: 150, width: 210, height: 50))
        nameEdit.placeholder = "请输入您的姓名"
        self.view.addSubview(name)
        self.view.addSubview(nameEdit)
        
        let birthday = UILabel(frame: CGRect(x: 20, y: 200, width: 70, height: 50))
        birthday.text = "生日"
        birthdayEdit = UITextField(frame: CGRect(x: 100, y: 200, width: 210, height: 50))
        birthdayEdit.placeholder = "请输入您的生日"
        self.view.addSubview(birthday)
        self.view.addSubview(birthdayEdit)
        
        datePicker = UIDatePicker(frame: CGRect(x:0, y:300, width:320, height:216))
        datePicker.locale = NSLocale(localeIdentifier: "zh_cn") as Locale
        datePicker.timeZone = NSTimeZone.system
        datePicker.datePickerMode = UIDatePickerMode.date
        datePicker.addTarget(self, action: #selector(getDate), for: UIControlEvents.valueChanged)
        datePicker.layer.backgroundColor = UIColor.white.cgColor
        datePicker.layer.masksToBounds = true
        birthdayEdit.inputView = datePicker
        
        
        let sex = UILabel(frame: CGRect(x: 20, y: 250, width: 70, height: 50))
        sex.text = "性别"
        self.view.addSubview(sex);
        
        
        let titleArr:NSArray = ["男","女"];
        let bgView = SingleSelBtnView.init();
        bgView.hSingleSelBtn(titleArray: titleArr, typeE: 1);
        self.view.addSubview(bgView);
        
        
        let head = UILabel(frame: CGRect(x: 20, y: 300, width: 70, height: 50))
        head.text = "头像"
        self.view.addSubview(head);
        
        let imageView = UIImageView(image:UIImage(named:"head.png"))
        imageView.frame = CGRect(x:100, y:310, width:80, height:80)
        self.view.addSubview(imageView)
        imageView.isUserInteractionEnabled = true;
        let toBig = UITapGestureRecognizer(target:self, action: #selector(RegisterViewController.big))
        imageView.addGestureRecognizer(toBig)
        
        let school = UILabel(frame: CGRect(x: 20, y: 400, width: 70, height: 50))
        school.text = "学校"
        schoolEdit = UITextField(frame: CGRect(x: 100, y: 400, width: 210, height: 50))
        schoolEdit.placeholder = "请选择您的学校"
        self.view.addSubview(school)
        self.view.addSubview(schoolEdit)
        
        keys = Array(countries.keys).sorted()
        let screenRect = UIScreen.main.bounds
        let tableRect = CGRect(x: 0, y: 20, width: screenRect.size.width, height: screenRect.size.height - 20)
        let tableView = UITableView(frame: tableRect)
        schoolEdit.inputView = tableView
        tableView.dataSource = self
        tableView.delegate = self
        
        
        let sure = UIButton(frame: CGRect(x: 100, y: 460, width: 100, height: 44))
        sure.setTitle("确认", for: UIControlState())
        sure.backgroundColor = UIColor.black
        sure.addTarget(self, action: #selector(RegisterViewController.dismissSelf), for: .touchUpInside)
        self.view.addSubview(sure)
        
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return keys.count
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        let subCountries = countries[keys[section]]
        return (subCountries?.count)!
    }
    
    @objc func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return keys[section]
    }
    
    @objc func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return keys
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let identifier = "reusedCell"
        var cell = tableView.dequeueReusableCell(withIdentifier: identifier)
        
        if(cell == nil){
            cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: identifier)
        }
        
        let subCountries = countries[keys[(indexPath as NSIndexPath).section]]
        cell?.textLabel?.text = subCountries![(indexPath as NSIndexPath).row]
        
        return cell!
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath)
        tableView.removeFromSuperview()
        schoolEdit.text = "\(String(describing: cell!.textLabel!.text!))"
        
    }
    
    @objc func dismissSelf()
    {
        let loginViewController = LoginViewController()
        loginViewController.viewController = self
        self.present(loginViewController, animated: true, completion: nil)
    }
    
    //将图片放大显示
    @objc func big()
    {
        uiview = UIView(frame: CGRect(x: 0, y: 0, width: 500, height: 600))
        uiview.backgroundColor = UIColor.black
        let imageView = UIImageView(image:UIImage(named:"head.png"))
        imageView.frame = CGRect(x:60, y:150, width:200, height:200)
        imageView.isUserInteractionEnabled = true;
        let toSmall = UITapGestureRecognizer(target:self, action: #selector(RegisterViewController.small))
        imageView.addGestureRecognizer(toSmall)
        uiview.addSubview(imageView)
        self.view.addSubview(uiview)
    }
    
    //把放大后的图片重新缩小
    @objc func small(){
        uiview.removeFromSuperview()
    }
    
    
    //获取选中的日期
    @objc func getDate(datePicker: UIDatePicker) {
        let formatter = DateFormatter()
        let date = datePicker.date
        formatter.dateFormat = "yyyy年MM月dd日"
        let dateStr = formatter.string(from: date)
        self.birthdayEdit.text = dateStr
    }
    
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
