//
//  EcosystemDataSource.swift
//  Ecosystem Feed
//
//  Created by Damla Karakulah on 22.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import Foundation

protocol EcosystemDataDelegate {
    func ecosystemListLoaded(ecosystemList : [Ecosystem])
}

extension EcosystemDataDelegate{
    func ecosystemListLoaded(ecosystemList : [Ecosystem]) { }
}

class EcosystemDataSource : NSObject {
    
    var delegate : EcosystemDataDelegate?
    
    func loadEcosystemList(groupid : String)
    {
        
        let session = URLSession.shared
        
        var request = URLRequest(url: URL(string: "http://ecosystemfeed.com/Service/Web.php?process=getEcosystem&groupid=\(groupid)")!)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "GET"
                
        let dataTask = session.dataTask(with: request) { (data, response, error) in
            let decoder = JSONDecoder()
            do {
                let ecosystemArray = try decoder.decode([Ecosystem].self, from: data!)
                
                self.delegate?.ecosystemListLoaded(ecosystemList: ecosystemArray)
            }
            catch{
                self.delegate?.ecosystemListLoaded(ecosystemList: [])
                print("no ecosystems")
            }
        }
        
        dataTask.resume()
    }
    
}
