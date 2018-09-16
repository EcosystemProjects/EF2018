//
//  PopUpViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 2.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class PopUpViewController: UIViewController {
    
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var pickView: UIView!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        pickView.layer.cornerRadius = 4
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        
        
    }

    @IBAction func okButton(_ sender: Any) {
        dismiss(animated: true)
        
        
    }
    
}
