//
//  CategoriesViewController.swift
//  Ecosystem Feed
//
//  Created by Damla Karakulah on 22.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class CategoriesViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, CategoryDataDelegate {
    
    @IBOutlet weak var ecosystemNameLabel: UILabel!
    
    @IBOutlet weak var tableView: UITableView!
    
    let dataSource = CategoryDataSource()
    var categoryArray : [Category] = []
    var selectedEcosystem : String = ""
    var selectedEcosystemName : String = ""

    
    func categoryListLoaded(categoryList: [Category]) {
        self.categoryArray = categoryList
        DispatchQueue.main.async {
            self.ecosystemNameLabel.text = "\(self.selectedEcosystemName)"
            self.tableView.reloadData()
        }
    }

    override func viewDidLoad() {
        self.title = "Categories"
        self.dataSource.delegate = self
        self.tableView?.rowHeight = 135
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        dataSource.loadCategoryList(groupid: self.selectedEcosystem)
        
    }
    
    @IBAction func quit(_ sender: Any) {
        dismissThisController()
    }
    
    func dismissThisController()
    {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5) {
            self.dismiss(animated: true, completion: {})
        }
    }

    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return categoryArray.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let reusableCell = tableView.dequeueReusableCell(withIdentifier: "CategoryCell", for: indexPath)
            as! CategoryTableViewCell
        
        let category = self.categoryArray[indexPath.row % categoryArray.count]
        reusableCell.nameLabel.text = "\(category.name)"
      
        return reusableCell
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
