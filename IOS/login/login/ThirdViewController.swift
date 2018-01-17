//
//  ThirdActivityController.swift
//  login
//
//  Created by Student09 on 2017/12/7.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class ThirdViewController: UIViewController {
    
    var webView:UIWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor.white
        
        let label = UILabel(frame: CGRect(x: 40, y: 60, width: 240, height: 50))
        label.text = "附加部分:webView"
        label.textAlignment = NSTextAlignment.center
        label.font = UIFont(name: "Arial", size: 16)
        self.view.addSubview(label)
        
        let bounds = UIScreen.main.bounds
        let frame = CGRect(x: 10, y: 130, width: bounds.width-20, height: 200)
        webView = UIWebView(frame: frame)
        webView.backgroundColor = UIColor.clear
        self.view.addSubview(webView)
        
        let path = Bundle.main.path(forResource: "me", ofType: "html")
        let url = URL( string: path!)
        webView.loadRequest(NSURLRequest(url: url!) as URLRequest)
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}

