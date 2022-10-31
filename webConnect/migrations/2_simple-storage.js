let simpleStorage = artifacts.require("simpleStorage");

module.exports = function (deployer) {
  deployer.deploy(simpleStorage);
};
