source "https://rubygems.org"

gem "fastlane", ">= 2.226.0"

# Ruby 3.4 compatibility: Add standard libraries that were removed from default gems
gem "base64"
gem "bigdecimal"
gem "mutex_m"
gem "abbrev"
gem "observer"
gem "racc"
gem "drb"
gem "csv"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
