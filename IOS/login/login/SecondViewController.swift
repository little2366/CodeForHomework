//
//  SecondActivityController.swift
//  login
//
//  Created by Student09 on 2017/12/7.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class SecondViewController: UIViewController {
    
    var imageView : UIImageView!
    var isTouchInImageView : Bool = false
    var currentTag: Int = 1
    var img = ["page1", "page2", "page3"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor.white
        
        let image = UIImage(named: img[0])
        imageView = UIImageView(image: image)
        imageView.frame = CGRect(x: 60, y: 20, width: 200, height: 200)
        imageView.tag = 1
        self.view.addSubview(imageView)
        
        let label = UILabel(frame: CGRect(x: 40, y: 300, width: 240, height: 50))
        label.text = "滑动翻页"
        label.textAlignment = NSTextAlignment.center
        label.font = UIFont(name: "Arial", size: 16)
        self.view.addSubview(label)

        
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        let touchPoint = touch?.location(in: self.view)
        
        let minX = imageView.frame.origin.x
        let minY = imageView.frame.origin.y
        let maxX = minX + imageView.frame.size.width
        let maxY = minY + imageView.frame.size.height
        if (touchPoint?.x)! >= minX && (touchPoint?.y)! <= maxX && (touchPoint?.y)! >= minY && (touchPoint?.y)! <= maxY
        {
            isTouchInImageView = true
            playAnimation()
        }
    }
    
    
    func playAnimation()
    {
        
        UIView.beginAnimations(nil, context: nil)
        UIView.setAnimationCurve(.easeOut)
        UIView.setAnimationDuration(5)
        UIView.setAnimationBeginsFromCurrentState(true)
        
        let view = self.view.viewWithTag(currentTag)
        UIView.setAnimationTransition(.curlUp, for: view!, cache: true)
        UIView.setAnimationDelegate(self)
        UIView.setAnimationDidStop(#selector(ViewController.animationStop))
        
        UIView.commitAnimations()
    }
    
    func animationStop()
    {
        if(currentTag < 3){
            let image = UIImage(named: img[currentTag])
            self.view.viewWithTag(currentTag)?.removeFromSuperview()
            
            currentTag = currentTag + 1
            imageView = UIImageView(image: image)
            imageView.frame = CGRect(x: 60, y: 20, width: 200, height: 200)
            imageView.tag = currentTag
            imageView.isUserInteractionEnabled = true
            self.view.addSubview(imageView)
        }
        
    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
