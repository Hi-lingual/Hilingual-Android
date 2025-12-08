source "https://rubygems.org"

gem "fastlane"

# Standard libraries removed from default gems in Ruby 3.4
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
