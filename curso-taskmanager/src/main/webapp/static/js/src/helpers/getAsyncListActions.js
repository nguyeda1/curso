const getActions = types => {
  return {
    fetch: meta => {
      return {
        type: types.FETCH,
        meta
      };
    },
    fetchFulfilled: response => {
      return {
        type: types.FETCH_FULFILLED,
        payload: response.data
      };
    },
    fetchError: error => {
      return {
        type: types.FETCH_ERROR,
        payload: error
      };
    },
    fetchNext: meta => {
      return {
        type: types.FETCH_NEXT,
        meta
      };
    },
    fetchNextFulfilled: response => {
      return {
        type: types.FETCH_NEXT_FULFILLED,
        payload: response.data
      };
    },
    fetchNextError: error => {
      return {
        type: types.FETCH_NEXT_ERROR,
        payload: error
      };
    },

    refresh: meta => {
      return { type: types.REFRESH, meta };
    },

    refreshFulfilled: (response, meta) => {
      return { type: types.REFRESH_FULFILLED, payload: response.data, meta };
    },

    refreshError: error => {
      return { type: types.REFRESH_ERROR, payload: error };
    },
    search: meta => {
      return {
        type: types.SEARCH,
        meta
      };
    },
    searchFulfilled: response => {
      return {
        type: types.SEARCH_FULFILLED,
        payload: response.data
      };
    },
    searchError: error => {
      return {
        type: types.SEARCH_ERROR,
        payload: error
      };
    },
    searchById: meta => {
      return {
        type: types.SEARCH_BY_ID,
        meta
      };
    },
    searchByIdFulfilled: response => {
      return {
        type: types.SEARCH_BY_ID_FULFILLED,
        payload: response.data
      };
    },
    searchByIdError: error => {
      return {
        type: types.SEARCH_BY_ID_ERROR,
        payload: error
      };
    },

    update: () => {
      return {
        type: types.UPDATE
      };
    },

    updateFulfilled: response => {
      return { type: types.UPDATE_FULFILLED, payload: response.data };
    },

    updateError: error => {
      return { type: types.UPDATE_ERROR, payload: error };
    },
    add: () => {
      return {
        type: types.ADD_ITEM
      };
    },
    addFulfilled: response => {
      return { type: types.ADD_ITEM_FULFILLED, payload: response.data };
    },
    addError: error => {
      return { type: types.ADD_ITEM_ERROR, payload: error };
    }
  };
};

export default getActions;
