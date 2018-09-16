//
//  feedCell.swift
//  Ecosystem Feed
//
//  Created by Ahmet Buğra Peşman on 17.07.2018.
//  Copyright © 2018 Ahmet Buğra Peşman. All rights reserved.
//

import UIKit

class feedCell: UITableViewCell {

    
    @IBOutlet weak var postUudiLabel: UILabel!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var postCommentText: UITextView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        postUudiLabel.isHidden = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
