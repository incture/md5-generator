var crypto = require('crypto');

module.exports = {

	// now use var imported.md5(string) to get your md5 hashed string

	// more functionality will be added soon
	
	'md5': function (input) {

		return crypto.createHash('md5')
		.update(input)
		.digest('hex');
	},
	

	'sha256': function(input, secret) {
		// sha256 hashing function added
		// by default secret hashing key is 'password'
		// though you can pass your own

		if (typeof(input)==='undefined') return null;
		if (typeof(secret)==='undefined') secret = "password";
		
		const hash = crypto.createHmac('sha256', secret)
                   .update(input)
                   .digest('hex');
		return hash;

	}


};
