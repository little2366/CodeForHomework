//
//  UIView.swift
//  login
//
//  Created by Student09 on 2017/12/21.
//  Copyright © 2017年 s. All rights reserved.
//

import UIKit

class SingleSelBtnView: UIView {
    
    let defaultFrame = CGRect(x:80, y:230, width:200, height:100)
    var currentTag: Int!
    
    init(){
        
        super.init(frame:defaultFrame)
        self.currentTag = 1
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    
    func hSingleSelBtn(titleArray:NSArray, typeE aTypeE:Int) {
        
        
        let frameE:CGRect = self.frame;
        let aWidthH:CGFloat = frameE.width;
        let aHeightT:CGFloat = frameE.height;
        
        if aTypeE==1 {
            //横向
            let widthH:CGFloat = (aWidthH-20-30*CGFloat(titleArray.count))/CGFloat(titleArray.count);
            
            for i:Int in 0 ..< titleArray.count {
                
                let btn = UIButton.init(frame: CGRect(x:10+(widthH+20)*CGFloat(i)+2, y:(aHeightT-16)/2-2, width:20, height:20));
                btn.setImage(UIImage.init(named: "unchoose"), for: .normal);
                btn.setImage(UIImage.init(named: "choose"), for: .selected);
                if i==0 {
                    btn.isSelected = true;
                }else{
                    btn.isSelected = false;
                }
                btn.tag = i+10000;
                btn.addTarget(self, action: #selector(btnClick), for: .touchUpInside);
                self.addSubview(btn);
                
                let labelL = UILabel.init(frame: CGRect(x:40+(widthH+20)*CGFloat(i), y:0, width:widthH, height:aHeightT));
                labelL.text = titleArray[i] as? String;
                labelL.adjustsFontSizeToFitWidth = true;
                self.addSubview(labelL);
            }
        }
        currentTag = 10000;
    }
    
    @objc func btnClick(btn:UIButton) {
        if !btn.isSelected {
            btn.isSelected = !btn.isSelected;
            //上一个按钮还原
            let buttonN = self.viewWithTag(currentTag) as? UIButton;
            buttonN?.isSelected = false;
            
            currentTag = btn.tag;
        }
    }
    
}

