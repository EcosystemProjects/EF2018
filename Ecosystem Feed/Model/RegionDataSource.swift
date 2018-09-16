//
//  EcosystemDataSource.swift
//  Ecosystem Feed
//
//  Created by Damla Karakulah on 20.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import Foundation

protocol RegionDataDelegate {
    func regionListLoaded(regionList : [Region])
}

extension RegionDataDelegate{
    func regionListLoaded(regionList : [Region]) { }
}

class RegionDataSource : NSObject {
    
    var delegate : RegionDataDelegate?
    
    func loadRegionList()
    {
        
        let session = URLSession.shared
        
        var request = URLRequest(url: URL(string: "http://ecosystemfeed.com/Service/Web.php?process=getRegion")!)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "GET"
        
        let dataTask = session.dataTask(with: request) { (data, response, error) in
            
            let decoder = JSONDecoder()
            
            do{
                let regionArray = try decoder.decode([Region].self, from: data!)
                self.delegate?.regionListLoaded(regionList: regionArray)
            }
            catch{
                print("no regions")
            }
            
        }
        
        dataTask.resume()
    }
    
}
