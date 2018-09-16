//
//  settingsViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 20.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class settingsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()


    }
    
   

    @IBAction func logOut1(_ sender: Any) {
        UserDefaults.standard.removeObject(forKey: "user")
        UserDefaults.standard.synchronize()
        
        let SignUp = self.storyboard?.instantiateViewController(withIdentifier: "SignUp") as? ViewController
        let delegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
        delegate.window?.rootViewController = SignUp
        delegate.rememberLogin()
    }
    
    
   
    

}
