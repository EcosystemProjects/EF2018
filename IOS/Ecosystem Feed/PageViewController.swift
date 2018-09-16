//
//  PageViewController.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 19.06.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class PageViewController: UIPageViewController, UIPageViewControllerDelegate, UIPageViewControllerDataSource {

    lazy var orderedViewControllers: [UIViewController] = {
        return [self.newVC(viewController: "sbAbout"),
                self.newVC(viewController: "sbContact"),
                self.newVC(viewController: "sbSettings")]
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dataSource = self
       if let firstViewController = orderedViewControllers.first {
            setViewControllers([firstViewController],
                               direction: .forward,
                               animated: true,
                               completion: nil)
        }
        
    }

    func newVC(viewController: String) -> UIViewController {
        return UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier:viewController)
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return self.orderedViewControllers.first
        }
        
        guard orderedViewControllers.count > previousIndex else {
            return nil
        }
        
        return orderedViewControllers[previousIndex]
     
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        
        guard orderedViewControllers.count != nextIndex else {
            return self.orderedViewControllers.first
        }
        
        guard orderedViewControllers.count > nextIndex else {
            return nil
        }
        
        return orderedViewControllers[nextIndex]
    }
    
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter1 viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let nextIndex1 = viewControllerIndex + 1
        
        guard orderedViewControllers.count != nextIndex1 else {
            return self.orderedViewControllers.first
        }
        
        guard orderedViewControllers.count > nextIndex1 else {
            return nil
        }
        
        return orderedViewControllers[nextIndex1]
    }
    
    
    
   

}
