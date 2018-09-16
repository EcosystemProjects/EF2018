//
//  CategoryDataSource.swift
//  Ecosystem Feed
//
//  Created by Damla Karakulah on 22.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import Foundation

protocol CategoryDataDelegate {
    func categoryListLoaded(categoryList : [Category])
}

extension CategoryDataDelegate{
    func categoryListLoaded(categoryList : [Category]) { }
}

class CategoryDataSource : NSObject {
    
    var delegate : CategoryDataDelegate?
    
    func loadCategoryList(groupid : String)
    {
        
        let session = URLSession.shared
        
        var request = URLRequest(url: URL(string: "http://ecosystemfeed.com/Service/Web.php?process=getCategories&groupid=\(groupid)")!)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "GET"
        
        let dataTask = session.dataTask(with: request) { (data, response, error) in
            let decoder = JSONDecoder()
            do {
                let categoryArray = try decoder.decode([Category].self, from: data!)
                
                self.delegate?.categoryListLoaded(categoryList: categoryArray)
            }
            catch{
                self.delegate?.categoryListLoaded(categoryList: [])
                print("no categories")
            }
        }
        
        dataTask.resume()
    }
    
}

