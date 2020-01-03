// check if an element exists in array using a comparer function
// comparer : function(currentElement)
Array.prototype.inArray = function(comparer) {
  for (var i = 0; i < this.length; i++) {
    if (comparer(this[i])) return true;
  }
  return false;
};

// adds an element to the array if it does not already exist using a comparer
// function
Array.prototype.pushIfNotExist = function(element, comparer) {
  if (!this.inArray(comparer)) {
    this.push(element);
  } else {
    console.log(element);
  }
};

// adds an element to the array if it does not already exist using a comparer
// function
Array.prototype.insertIfNotExist = function(element, comparer) {
  if (!this.inArray(comparer)) {
    this.unshift(element);
  }
};

export const pushUniqueElements = (array, newArray) => {
  var res = [...array];
  newArray.map(object => {
    res.pushIfNotExist(object, other => {
      return other.id === object.id;
    });
  });
  return res;
};

export const insertUniqueElements = (array, newArray) => {
  var res = [...array];
  if (newArray) {
    newArray.map(object => {
      res.insertIfNotExist(object, other => {
        return other.id === object.id;
      });
    });
  }
  return res;
};
