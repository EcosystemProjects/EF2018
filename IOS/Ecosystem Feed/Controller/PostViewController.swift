//
//  fouthViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 19.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class PostViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIPickerViewDelegate,UIPickerViewDataSource, RegionDataDelegate, CategoryDataDelegate, EcosystemDataDelegate {
    @IBOutlet weak var link: UITextView!
    
    @IBOutlet weak var edit: UIButton!
    
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var postButton: UIButton!
    
    @IBOutlet weak var titleF: UITextView!
    @IBOutlet weak var descF: UITextView!
    
    @IBOutlet weak var pick1: UIPickerView!
    @IBOutlet weak var pick2: UIPickerView!
    @IBOutlet weak var pick3: UIPickerView!
    @IBOutlet weak var viewI: UIView!
    
    let dataSource = RegionDataSource()
    var regionArray : [Region] = []
    
    
    let dataSource2 = EcosystemDataSource()
    var ecosystemArray : [Ecosystem] = []
    
    let dataSource3 = CategoryDataSource()
    var categoryArray : [Category] = []
    
    var selectedRegion : Region?
    var selectedEcosystem : Ecosystem?
    var selectedCategory : Category?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let keyboardRecognizer = UITapGestureRecognizer(target: self, action: #selector(PostViewController.hideKeyboard))
        self.view.addGestureRecognizer(keyboardRecognizer)

        postImage.isUserInteractionEnabled = true
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(PostViewController.choosePhoto))
        postImage.addGestureRecognizer(gestureRecognizer)
        
        self.link.layer.shadowRadius = 9
        self.link.layer.cornerRadius = 9
        self.link.layer.masksToBounds = false
        self.link.layer.shadowOpacity = 0.3
        self.titleF.layer.cornerRadius = 9
        self.descF.layer.cornerRadius = 9
        self.viewI.layer.cornerRadius = 9
        
        pick1.delegate = self
        pick1.dataSource = self
        
        pick2.delegate = self
        pick2.dataSource = self
        
        pick3.delegate = self
        pick3.dataSource = self
        
        pick1.tag = 1
        pick2.tag = 2
        pick3.tag = 3
        
        self.dataSource.delegate = self
        self.dataSource2.delegate = self
        self.dataSource3.delegate = self
        
  
    }
    
    override func viewWillAppear(_ animated: Bool) {
        dataSource.loadRegionList()
    }
    
    func regionListLoaded(regionList: [Region]) {
        self.regionArray = regionList
            DispatchQueue.main.async {
                self.pick1.reloadAllComponents()
            }
    }
    
    func ecosystemListLoaded(ecosystemList: [Ecosystem]) {
        self.ecosystemArray = ecosystemList
        DispatchQueue.main.async {
            self.pick2.reloadAllComponents()
        }
        print("Ecosystem Array Count:")
        print(ecosystemList.count)
    }
    
    func categoryListLoaded(categoryList: [Category]) {
        self.categoryArray = categoryList
        DispatchQueue.main.async {
            self.pick3.reloadAllComponents()
        }
        print("Category Array Count:")
        print(categoryList.count)

        
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if(pickerView.tag == 1){
            return regionArray.count
        }
        else if(pickerView.tag == 2){
            return ecosystemArray.count
        }
        else{
            return categoryArray.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if(pickerView.tag == 1){
            return regionArray[row].name
            
        }
        else if(pickerView.tag == 2){
            return ecosystemArray[row].name
        }
        else{
            return categoryArray[row].name
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if(pickerView.tag == 1){
            self.selectedRegion = regionArray[row]
            dataSource2.loadEcosystemList(groupid: (selectedRegion?.id)!)
            ecosystemListLoaded(ecosystemList: ecosystemArray)
            
        }
        else if(pickerView.tag == 2){
            self.selectedEcosystem = ecosystemArray[row]
            dataSource3.loadCategoryList(groupid: (selectedEcosystem?.id)!)
            categoryListLoaded(categoryList: categoryArray)

        }
        else{
            self.selectedCategory = categoryArray[row]
        }
        
    }
 
    @IBAction func uploadButton(_ sender: Any) {
        choosePhoto()
    }
    
    @objc func hideKeyboard() {
        self.view.endEditing(true)
    }
    
    @objc func choosePhoto() {
        
        let pickerController = UIImagePickerController()
        pickerController.delegate = self
        pickerController.sourceType = .photoLibrary
        pickerController.allowsEditing = true
        present(pickerController, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        postImage.image = info[UIImagePickerControllerEditedImage] as? UIImage
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func postButtonClicked(_ sender: Any) {
        self.titleF.text = ""
        self.postImage.image = UIImage(named: "select.png")
        self.tabBarController?.selectedIndex = 0
    }
    

}


















