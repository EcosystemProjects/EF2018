//
//  regionViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 23.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class EcosystemsViewController: UIViewController, EcosystemDataDelegate {

    @IBOutlet weak var mScrollView: UIScrollView!
    let dataSource = EcosystemDataSource()
    var ecosystemArray : [Ecosystem] = []
    var colorArray : [UIColor] = []
    var selectedRegion : String = ""
    var selectedEcosystem : String = ""
    var selectedEcosystemName : String = ""


    @IBAction func quit(_ sender: Any) {
        dismissThisController()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource.delegate = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        dataSource.loadEcosystemList(groupid: self.selectedRegion)
        colorArray = [UIColor.green,UIColor.purple,UIColor.orange,UIColor.blue,UIColor.red,UIColor.cyan,UIColor.lightGray]
        
    }
    
    func ecosystemListLoaded(ecosystemList: [Ecosystem]) {
        self.ecosystemArray = ecosystemList
        setButtons()
    }
    
    @objc func scrollButtonAction(sender: UIButton) {
        print("\(ecosystemArray[sender.tag].name) is Selected")
        self.selectedEcosystem = ecosystemArray[sender.tag].id
        self.selectedEcosystemName = ecosystemArray[sender.tag].name
        performSegue(withIdentifier: "Categories", sender: self)
    }
    
    func setButtons(){
        DispatchQueue.main.async {

            let numberOfButtons = self.ecosystemArray.count
            let px = self.mScrollView.center.x
            var py = 100
            if(self.ecosystemArray.count == 0){
                return
            }
            for count in 0...numberOfButtons-1 {
                
                let button = UIButton()
                button.tag = count
                button.frame = CGRect(x:  Int(px)-(Int(self.mScrollView.contentSize.width-90)/2), y:py, width: Int(self.mScrollView.contentSize.width-90), height: 45)
                button.backgroundColor = UIColor.lightGray
                button.setTitle("\(self.self.ecosystemArray[count].name)", for: .normal)
                button.layer.cornerRadius = 9
                if(count >= 0){
                    button.backgroundColor = self.colorArray[count%self.colorArray.count]
                }
                button.addTarget(self, action: #selector(self.scrollButtonAction), for: .touchUpInside)
                self.mScrollView.addSubview(button)
                py += 50
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if (segue.destination is CategoriesViewController){
            let categoriesController = segue.destination as! CategoriesViewController
            categoriesController.selectedEcosystem = self.selectedEcosystem
            categoriesController.selectedEcosystemName = self.selectedEcosystemName
        }
        else{
            print("error occured")
        }
        
    }
    
    func dismissThisController()
    {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5) {
            self.dismiss(animated: true, completion: {})
        }
    }
    

}
