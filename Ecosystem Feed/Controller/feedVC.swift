//
//  feedVC.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 17.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class feedVC: UIViewController, UITableViewDelegate, UITableViewDataSource {

    
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        let keyboardRecognizer = UITapGestureRecognizer(target: self, action: #selector(feedVC.hideKeyboard))
        self.view.addGestureRecognizer(keyboardRecognizer)
        tableView.delegate = self
        tableView.dataSource = self

    }
    @objc func hideKeyboard() {
        self.view.endEditing(true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! feedCell
        cell.postUudiLabel.isHidden = true
        return cell
    }
}

