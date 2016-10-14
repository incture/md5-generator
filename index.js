var crypto = require('crypto');

module.exports = {

	// now use var imported.md5(string) to get your md5 hashed string
	// more functionality will be added soon
	
	'md5': function (input) { 
		return crypto.createHash('md5').update(input).digest('hex');
	}

};
