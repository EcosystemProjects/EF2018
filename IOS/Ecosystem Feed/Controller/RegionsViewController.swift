//
//  secondViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 18.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class RegionsViewController: UIViewController, RegionDataDelegate {
    
    @IBOutlet weak var mScrollView: UIScrollView!
    let dataSource = RegionDataSource()
    var regionArray : [Region] = []
    var selectedRegion : String = ""


    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource.delegate = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        dataSource.loadRegionList()
    }
    
    func regionListLoaded(regionList: [Region]) {
        self.regionArray = regionList
        setButtons()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func scrollButtonAction(sender: UIButton) {
        print("\(regionArray[sender.tag].name) is Selected")
        self.selectedRegion = (regionArray[sender.tag].id)
        
        self.performSegue(withIdentifier: "Ecosystems", sender: self)
        
    }
    
    func setButtons(){
         DispatchQueue.main.async {
            let numberOfButtons = self.regionArray.count
            let px = self.self.mScrollView.center.x
            var py = 100
            
            if(self.regionArray.count == 0){
                return
            }
            
            for count in 0...numberOfButtons-1 {
            
            let button = UIButton()
            button.tag = count
            button.frame = CGRect(x: Int(px)-(Int(self.mScrollView.contentSize.width-10)/2), y:py, width: Int(self.mScrollView.contentSize.width-10), height: 45)
            button.backgroundColor = UIColor.lightGray
            button.setTitle("\(self.self.regionArray[count].name)", for: .normal)
            
            button.layer.cornerRadius = 9
                
            button.addTarget(self, action: #selector(self.scrollButtonAction), for: .touchUpInside)
                
            self.mScrollView.addSubview(button)
            py += 50
        }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
         if (segue.destination is EcosystemsViewController){
            let ecosystemController = segue.destination as! EcosystemsViewController
            ecosystemController.selectedRegion = self.selectedRegion
        }
         else{
            print("error occured")
        }
        
    }

}
