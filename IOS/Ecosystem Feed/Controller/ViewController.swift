//
//  ViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 18.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var byMentornity: UILabel!
    @IBOutlet weak var ecosystemFeed: UILabel!
    @IBOutlet weak var followCategories: UILabel!
    @IBOutlet weak var connectVia: UIButton!
    @IBOutlet weak var browseEco: UIButton!
    @IBOutlet weak var termsOf: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        connectVia.layer.cornerRadius = 10
        browseEco.layer.borderWidth = 1
        browseEco.layer.cornerRadius = 10
       
   
    }

   
    @IBAction func connecVia(_ sender: Any) {
        LISDKSessionManager.createSession(withAuth: [LISDK_BASIC_PROFILE_PERMISSION], state: nil, showGoToAppStoreDialog: true, successBlock: { (success) in
            let url = "https://api.linkedin.com/v1/people/~"
            if(LISDKSessionManager.hasValidSession()){
                LISDKAPIHelper.sharedInstance().getRequest(url, success: { (response) in
                    let dict = self.stringToDictionary(text: (response?.data)!)
                    print("your last name is \(dict?["lastname"])")
                    UserDefaults.standard.set(true, forKey: "user")
                    UserDefaults.standard.synchronize()
                    let delegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
                    delegate.rememberLogin()
                }, error: { (error) in
                    print(error)
                })
  
            }
        }) { (error) in
            print("Error\(error)")
        }
        
        
       
    }
    func stringToDictionary(text: String)-> [String: Any]?{
        if let data = text.data(using: .utf8){
            do{
                return (try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any])!
            }catch{
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    
    
}

