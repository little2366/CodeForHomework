//
//  FirstActivityController.swift
//  login
//
//  Created by Student09 on 2017/12/7.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor.white
        
        let label = UILabel(frame: CGRect(x: 40, y: 100, width: 240, height: 50))
        label.text = "捏合手势:图片的放大和缩小"
        label.textAlignment = NSTextAlignment.center
        label.font = UIFont(name: "Arial", size: 18)
        self.view.addSubview(label)
        
        let rect = CGRect(x:0, y:180, width:320, height:320)
        let imageView = UIImageView(frame: rect)
        
        let image = UIImage(named: "image1")
        imageView.image = image
        
        imageView.isUserInteractionEnabled = true
        self.view.addSubview(imageView)
        
        let gesture = UIPinchGestureRecognizer(target:self, action:#selector(FirstViewController.pinchImage))
        imageView.addGestureRecognizer(gesture)
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func pinchImage(_recognizer:UIPinchGestureRecognizer){
        _recognizer.view?.transform = (_recognizer.view?.transform.scaledBy(x:_recognizer.scale, y:_recognizer.scale))!
        _recognizer.scale = 1
    }
    
}
