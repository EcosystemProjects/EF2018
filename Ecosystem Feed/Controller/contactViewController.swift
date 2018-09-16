//
//  contactViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 20.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class contactViewController: UIViewController {

    @IBAction func Website(_ sender: Any) {
        if let url = NSURL(string: "https://mentornity.com/"){
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()


    }
    
    @IBAction func facebook(_ sender: Any) {
        if let url = NSURL(string: "https://www.facebook.com/mentornity/?ref=br_rs"){
             UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    
    @IBAction func twitter(_ sender: Any) {
        if let url = NSURL(string: "https://twitter.com/mentornity"){
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    @IBAction func linkedin(_ sender: Any) {
        if let url = NSURL(string: "https://www.linkedin.com/company/mentornity/"){
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    
    @IBAction func mail(_ sender: Any) {
        if let url = URL(string: "mailto:ef@mentornity.com"){
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
    }
    
    
    
}
