//
//  myPostViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 1.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class myPostViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

    }

    @IBAction func create(_ sender: Any) {
        performSegue(withIdentifier: "cr", sender: nil)

    }
    
}
