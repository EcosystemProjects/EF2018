//
//  thirdViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 19.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class thirdViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
       

    }
    
    @IBAction func settingsButton(_ sender: Any) {
        performSegue(withIdentifier: "aboutToSettings", sender: nil)
    }
    
}
