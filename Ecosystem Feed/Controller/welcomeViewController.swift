//
//  welcomeViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 20.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class welcomeViewController: UIViewController {

    @IBOutlet weak var getStarted: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        getStarted.layer.cornerRadius = 10
        
        
    }

    @IBAction func logOut(_ sender: Any) {
        UserDefaults.standard.removeObject(forKey: "user")
        UserDefaults.standard.synchronize()
        
        let SignUp = self.storyboard?.instantiateViewController(withIdentifier: "SignUp") as? ViewController
        let delegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
        delegate.window?.rootViewController = SignUp
        delegate.rememberLogin()
    }
    
    

}
